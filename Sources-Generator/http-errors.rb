require 'fileutils'

module HTTPErrors
    class Logic
        HTTP_ERRORS_LIST = {
            400 => 'Bad Request',
            404 => 'Not Found',
            405 => 'Method Not Allowed',
            500 => 'Internal Server Error',
            501 => 'Not Implemented'
        }
        PACKAGE_DIR = real_path "#{GEN_DIR}/github/dmitmel/raketaframework/web/errors"

        def create_packages
            FileUtils.mkdir_p(PACKAGE_DIR)
        end

        def create_files
            for status in HTTP_ERRORS_LIST.keys
                content = <<JAVA
package github.dmitmel.raketaframework.web.errors;

public class Error#{status} extends HTTPError {
    public Error#{status}() {
        super();
    }

    @Override
    public int getCode() {
        return #{status};
    }

    @Override
    public String getDescription() {
        return "#{HTTP_ERRORS_LIST[status]}";
    }
}
JAVA
                File.write("#{PACKAGE_DIR}/Error#{status}.java", content)
            end

            content = "package github.dmitmel.raketaframework.web.errors;

import java.util.Map;
import java.util.HashMap;

public class DefaultErrorResponderMapMaker {
    public static Map<Integer, ErrorResponder> makeMap() {
        HashMap<Integer, ErrorResponder> map = new HashMap<>(#{HTTP_ERRORS_LIST.length});\n"
            for status in HTTP_ERRORS_LIST.keys
                content += "        map.put(#{status}, DefaultErrorResponderFactory.makeResponder());   // #{HTTP_ERRORS_LIST[status]}\n"
            end
            content += "        return map;
    }
}\n"
            File.write("#{PACKAGE_DIR}/DefaultErrorResponderMapMaker.java", content)
        end
    end
    private_constant :Logic

    def self.generate
        logic = Logic.new
        logic.create_packages
        logic.create_files
    end
end

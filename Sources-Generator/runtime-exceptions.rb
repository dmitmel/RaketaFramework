require 'fileutils'
require 'json'

module RuntimeExceptions
    class Logic
        RUNTIME_EXCEPTIONS_LIST = JSON.parse(File.read(real_path './runtime-exceptions.json'))
        PACKAGE_DIR = real_path "#{GEN_DIR}/github/dmitmel/raketaframework/util/exceptions"

        def create_packages
            FileUtils.mkdir_p(PACKAGE_DIR)
        end

        def create_files
            for name in RUNTIME_EXCEPTIONS_LIST.keys
                jdk_package = RUNTIME_EXCEPTIONS_LIST[name]['jdk-package']
                parent = RUNTIME_EXCEPTIONS_LIST[name]['parent'] || 'RuntimeException'
                content = <<JAVA
package github.dmitmel.raketaframework.util.exceptions;

/**
 * {@link #{jdk_package}.#{name}}
 */
public class #{name} extends #{parent} {
    public #{name}() {
    }

    public #{name}(String message) {
        super(message);
    }

    public #{name}(String message, Throwable cause) {
        super(message, cause);
    }

    public #{name}(Throwable cause) {
        super(cause);
    }

    public static #{name} extractFrom(#{jdk_package}.#{name} e) {
        return new #{name}(e.getMessage(), e.getCause());
    }
}
JAVA
                File.write("#{PACKAGE_DIR}/#{name}.java", content)
            end
        end
    end
    private_constant :Logic

    def self.generate
        logic = Logic.new
        logic.create_packages
        logic.create_files
    end
end

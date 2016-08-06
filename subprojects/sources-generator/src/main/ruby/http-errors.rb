require 'fileutils'
require_relative 'utils'

module HTTPErrors
    class Logic
        HTTP_ERRORS_LIST = {
            400 => 'Bad Request',
            404 => 'Not Found',
            405 => 'Method Not Allowed',
            500 => 'Internal Server Error',
            501 => 'Not Implemented'
        }
        PACKAGE_DIR = path_to_resource "#{GEN_DIR}/org/willthisfly/raketaframework/errors"
        HTTP_ERROR_TEMPLATE = File.read(path_to_resource "#{TEMPLATES_DIR}/HttpError.java.gentemplate")

        def create_packages
            FileUtils.mkdir_p(PACKAGE_DIR)
        end

        def create_files
            for status in HTTP_ERRORS_LIST.keys
                content = HTTP_ERROR_TEMPLATE % {
                    :status => status,
                    :description => HTTP_ERRORS_LIST[status]
                }

                File.write("#{PACKAGE_DIR}/Error#{status}.java", content)
            end

            content = "package org.willthisfly.raketaframework.errors;

import java.util.Map;
import java.util.HashMap;

public class DefaultErrorResponderMapMaker {
    public static Map<Integer, ErrorResponder> makeMap() {
        HashMap<Integer, ErrorResponder> map = new HashMap<>(#{HTTP_ERRORS_LIST.length});\n"
            for status in HTTP_ERRORS_LIST.keys
                content += "        map.put(#{status}, DefaultErrorResponderFactory.makeResponderForError(new Error#{status}()));   // #{HTTP_ERRORS_LIST[status]}\n"
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

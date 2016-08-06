require 'fileutils'
require 'json'

module RuntimeExceptions
    class Logic
        RUNTIME_EXCEPTIONS_LIST = JSON.parse(File.read(path_to_resource './runtime-exceptions.json'))
        PACKAGE_DIR = path_to_resource "#{GEN_DIR}/org/willthisfly/raketaframework/exceptions"
        TEMPLATE = File.read(path_to_resource "#{TEMPLATES_DIR}/RuntimeException.java.gentemplate")

        def create_packages
            FileUtils.mkdir_p(PACKAGE_DIR)
        end

        def create_files
            for name in RUNTIME_EXCEPTIONS_LIST.keys
                jdk_package = RUNTIME_EXCEPTIONS_LIST[name]['jdk-package']
                parent = RUNTIME_EXCEPTIONS_LIST[name]['parent'] || 'RuntimeException'

                content = TEMPLATE % {
                    :name => name,
                    :jdk_package => jdk_package,
                    :parent => parent
                }

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

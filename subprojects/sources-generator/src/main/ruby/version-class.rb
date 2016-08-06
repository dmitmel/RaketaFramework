require 'fileutils'

module VersionClass
    class Logic
        PACKAGE_DIR = path_to_resource "#{GEN_DIR}/org/willthisfly/raketaframework"
        TEMPLATE = File.read(path_to_resource "#{TEMPLATES_DIR}/Version.java.gentemplate")
        VERSION = File.read(File.expand_path '.version', ROOT_DIR).strip

        def create_packages
            FileUtils.mkdir_p(PACKAGE_DIR)
        end

        def create_files
            content = TEMPLATE % {
                :version => VERSION
            }

            File.write("#{PACKAGE_DIR}/Version.java", content)
        end
    end
    private_constant :Logic

    def self.generate
        logic = Logic.new
        logic.create_packages
        logic.create_files
    end
end

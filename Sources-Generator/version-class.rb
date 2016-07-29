require 'fileutils'

module VersionClass
    class Logic
        PACKAGE_DIR = real_path "#{GEN_DIR}/org/willthisfly/raketaframework"
        TEMPLATE = File.read(real_path "#{TEMPLATES_DIR}/Version.java.gentemplate")
        VERSION = File.read(real_path '../version.txt').strip

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

# Computes real path from relative. But, uses script's directory as root instead of CWD
def path_to_resource(path)
    path
    File.expand_path path, RESOURCES_ROOT
end

RESOURCES_ROOT = File.expand_path '../resources', File.dirname(__FILE__)
ROOT_DIR = File.expand_path '../../../../..', File.dirname(__FILE__)
GEN_DIR = path_to_resource "#{ROOT_DIR}/core/src/main/gen/java"
TEMPLATES_DIR = path_to_resource 'templates'

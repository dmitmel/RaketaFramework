# Computes real path from relative. But, uses script's directory as root instead of CWD
def real_path(path)
    File.expand_path(path, File.dirname(__FILE__))
end

GEN_DIR = real_path '../src/gen/java'
TEMPLATES_DIR = './templates'

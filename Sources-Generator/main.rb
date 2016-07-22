require 'fileutils'
require_relative 'utils'
require_relative 'http-errors'
require_relative 'runtime-exceptions'
require_relative 'version-class'

def clear_gen_dir
    FileUtils.rm_r GEN_DIR if File.exists? GEN_DIR
    FileUtils.mkdir_p GEN_DIR
end

clear_gen_dir
HTTPErrors.generate
RuntimeExceptions.generate
VersionClass.generate

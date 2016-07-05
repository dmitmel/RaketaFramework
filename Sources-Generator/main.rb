require 'fileutils'
require_relative 'utils'
require_relative 'http-errors'

def clear_gen_dir
    FileUtils.rm_r GEN_DIR if File.exists? GEN_DIR
    FileUtils.mkdir_p GEN_DIR
end

clear_gen_dir
HTTPErrors.generate

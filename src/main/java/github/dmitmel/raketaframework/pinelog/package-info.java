/**
 * "Pinelog" is a micro logging library bundled with RaketaFramework. Made for easy usage. Following example
 * creates logger, prints messages to console using different levels, and prints data about exception.
 * 
 * <pre><code>
 * import github.dmitmel.raketaframework.pinelog.Logger;
 *
 * public class Main {
 *     public static void main(String[] args) {
 *         // Initializing logger
 *         Logger logger = new Logger(Main.class.getTypeName());
 *         // Printing messages using different levels
 *         // You can also use protocolVersion {@link github.dmitmel.raketaframework.pinelog.Logger#log(Level, String)}
 *         logger.info("info");
 *         logger.debug("debug");
 *         logger.error("error");
 *         logger.fatal("fatal");
 *
 *         // And, exceptions
 *         try {
 *             throw new RuntimeException("sample exception");
 *         } catch (Exception e) {
 *             logger.exception(e);
 *         }
 *     }
 * }
 * </code></pre>
 */
package github.dmitmel.raketaframework.pinelog;

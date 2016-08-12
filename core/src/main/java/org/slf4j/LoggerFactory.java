package org.slf4j;


/**
 * Created by aleksey.kurnosenko on 11.08.16.
 */
public class LoggerFactory {



    public static Logger getLogger(Class<?> clazz) {
       return new Logger() {
           @Override
           public String getName() {
               return null;
           }

           @Override
           public boolean isTraceEnabled() {
               return false;
           }

           @Override
           public void trace(String msg) {

           }

           @Override
           public void trace(String format, Object arg) {

           }

           @Override
           public void trace(String format, Object arg1, Object arg2) {

           }

           @Override
           public void trace(String format, Object... arguments) {

           }

           @Override
           public void trace(String msg, Throwable t) {

           }

           @Override
           public boolean isDebugEnabled() {
               return false;
           }

           @Override
           public void debug(String msg) {
               System.out.println(msg);
           }

           @Override
           public void debug(String format, Object arg) {
               System.out.println(format);
           }

           @Override
           public void debug(String format, Object arg1, Object arg2) {
               System.out.println(format);
           }

           @Override
           public void debug(String format, Object... arguments) {
               System.out.println(format);
           }

           @Override
           public void debug(String msg, Throwable t) {
               System.out.println(msg);
           }

           @Override
           public boolean isInfoEnabled() {
               return false;
           }

           @Override
           public void info(String msg) {

           }

           @Override
           public void info(String format, Object arg) {

           }

           @Override
           public void info(String format, Object arg1, Object arg2) {

           }

           @Override
           public void info(String format, Object... arguments) {

           }

           @Override
           public void info(String msg, Throwable t) {

           }

           @Override
           public boolean isWarnEnabled() {
               return false;
           }

           @Override
           public void warn(String msg) {

           }

           @Override
           public void warn(String format, Object arg) {

           }

           @Override
           public void warn(String format, Object... arguments) {

           }

           @Override
           public void warn(String format, Object arg1, Object arg2) {

           }

           @Override
           public void warn(String msg, Throwable t) {

           }

           @Override
           public boolean isErrorEnabled() {
               return false;
           }

           @Override
           public void error(String msg) {

           }

           @Override
           public void error(String format, Object arg) {

           }

           @Override
           public void error(String format, Object arg1, Object arg2) {

           }

           @Override
           public void error(String format, Object... arguments) {

           }

           @Override
           public void error(String msg, Throwable t) {

           }
       };
    }
}

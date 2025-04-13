/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

 package io.cdap.wrangler.api;

 /**
  * Exception thrown when a record needs to be emitted to the error collector.
  * Contains error details including message, code, and display preferences.
  */
 public class ErrorRowException extends Exception {
   /** Error code associated with this exception. */
   private final int code;
 
   /** Flag indicating if error should be shown in wrangler. */
   private final boolean showInWrangler;
 
   /**
    * Constructs an ErrorRowException with name, message and code.
    *
    * @param name the directive name
    * @param message the descriptive error message
    * @param code the numeric error code
    */
   public ErrorRowException(final String name, final String message, final int code) {
     this(name + ": " + message, code, false);
   }

   /**
    * Constructs an ErrorRowException with name, message, code and cause.
    *
    * @param name the directive name
    * @param message the descriptive error message
    * @param code the numeric error code
    * @param cause the underlying cause
    */
   public ErrorRowException(final String name, final String message, final int code, final Throwable cause) {
     this(name + ": " + message, code, false, cause);
   }

   /**
    * Constructs an ErrorRowException with name, message and code (backward compatibility).
    *
    * @param name the directive name
    * @param message the descriptive error message
    * @param code the numeric error code
    * @deprecated Use {@link #ErrorRowException(String, String, int)} instead
    */
   @Deprecated
   public ErrorRowException(final String name, final String message, final int code, final boolean showInWrangler) {
     this(name + ": " + message, code, showInWrangler);
   }

   /**
    * Constructs an ErrorRowException with message and code.
    *
    * @param message the descriptive error message
    * @param code the numeric error code
    */
   public ErrorRowException(final String message, final int code) {
     this(message, code, false);
   }
 
   /**
    * Constructs an ErrorRowException with message, code and wrangler flag.
    *
    * @param message the descriptive error message
    * @param code the numeric error code
    * @param showInWrangler whether to show in wrangler UI
    */
   public ErrorRowException(final String message, final int code,
                           final boolean showInWrangler) {
     this(message, code, showInWrangler, null);
   }
 
   /**
    * Constructs an ErrorRowException with message, code, wrangler flag and cause.
    *
    * @param message the descriptive error message
    * @param code the numeric error code
    * @param showInWrangler whether to show in wrangler UI
    * @param cause the underlying cause of this exception
    */
   public ErrorRowException(final String message, final int code,
                           final boolean showInWrangler, final Throwable cause) {
     super(message, cause);
     this.code = code;
     this.showInWrangler = showInWrangler;
   }
 
   /**
    * Returns the error code associated with this exception.
    *
    * @return the numeric error code
    */
   public final int getCode() {
     return code;
   }
 
   /**
    * Indicates whether this error should be shown in the wrangler UI.
    *
    * @return true if should be shown in wrangler, false otherwise
    */
   public final boolean isShownInWrangler() { 
     return showInWrangler;
   }
 }

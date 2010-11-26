/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cosmocode.selenium;

/**
 * Value object for selenium host and port.
 * 
 * @author Tobias Sarnowski
 */
public interface SeleniumServer {

    /**
     * Retrieves this server's host.
     *
     * @return the server's hostname
     */
    String getHost();

    /**
     * Retrieves this server's port.
     *
     * @return the server's port
     */
    int getPort();

}


/*
 * Copyright (c) 2015, Michael Kühweg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.kuehweg.sqltool.database;

import de.kuehweg.sqltool.common.DialogDictionary;

/**
 * Liefert Standard-Verbindungsdaten, mit denen ohne explizite Verbindungsanlage durch den
 * Anwender gearbeitet werden kann.
 *
 * @author Michael Kühweg
 */
public class DefaultConnectionSettingsProvider {

    private DefaultConnectionSettingsProvider() {
    }

    /**
     * Eine In-Memory Verbinung wird grundsätzlich immer angeboten.
     *
     * @return
     */
    public static ConnectionSetting getDefaultInMemoryConnection() {
        return new ConnectionSetting(
                DialogDictionary.LABEL_DEFAULT_CONNECTION_IN_MEMORY.toString(),
                JDBCType.HSQL_IN_MEMORY, null, "rastelli", null, null);
    }

    /**
     * Eine dateibasierte Version im Benutzerverzeichnis.
     *
     * @return
     */
    public static ConnectionSetting getDefaultStandaloneUserHomeConnection() {
        return new ConnectionSetting(
                DialogDictionary.LABEL_DEFAULT_CONNECTION_STANDALONE_USER_HOME.toString(),
                JDBCType.HSQL_STANDALONE, System.getProperty("user.home") + "/itrysql",
                "standard_db", null, null);
    }

}
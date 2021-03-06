/*
 * Copyright (c) 2013, Michael Kühweg
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
package de.kuehweg.sqltool.dialog.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.FileUtil;
import de.kuehweg.sqltool.common.achievement.AchievementManager;
import de.kuehweg.sqltool.common.achievement.NamedAchievementEvent;
import de.kuehweg.sqltool.common.sqlediting.StatementExtractor;
import de.kuehweg.sqltool.common.sqlediting.StatementString;
import de.kuehweg.sqltool.database.execution.ResultRow;
import de.kuehweg.sqltool.database.execution.StatementExecution;
import de.kuehweg.sqltool.dialog.ConfirmDialog;
import de.kuehweg.sqltool.dialog.ErrorMessage;

/**
 * Aufbau der Tutorialdaten.
 *
 * @author Michael Kühweg
 */
public class TutorialAction {

	public void createTutorial(final ExecuteAction executeAction, final Connection connection) {
		if (confirmedByUser()) {
			createTutorialDataInDatabase(executeAction, connection);
		}
	}

	/**
	 * Rückfragedialog anzeigen und auswerten.
	 *
	 * @return true wenn der Anwender bestätigt, dass die Tutorialdaten
	 *         anegelegt werden sollen.
	 */
	private boolean confirmedByUser() {
		final ConfirmDialog confirm = new ConfirmDialog(DialogDictionary.MESSAGEBOX_CONFIRM.toString(),
				DialogDictionary.MSG_REALLY_CREATE_TUTORIAL_DATA.toString(),
				DialogDictionary.LABEL_CREATE_TUTORIAL_DATA.toString(),
				DialogDictionary.COMMON_BUTTON_CANCEL.toString());
		return DialogDictionary.LABEL_CREATE_TUTORIAL_DATA.toString().equals(confirm.askUserFeedback());
	}

	/**
	 * @param executeAction
	 * @param connection
	 */
	private void createTutorialDataInDatabase(final ExecuteAction executeAction, final Connection connection) {
		try {
			final String completeSql = new StringBuilder().append(buildStaticPortionOfScript())
					.append(buildDynamicPortionOfScript(connection)).toString();
			executeAction.handleExecuteAction(completeSql, connection);
			AchievementManager.getInstance().fireEvent(NamedAchievementEvent.TUTORIAL_BUILT.asAchievementEvent(), 1);
		} catch (final IOException | SQLException ex) {
			new ErrorMessage(DialogDictionary.MESSAGEBOX_ERROR.toString(),
					DialogDictionary.ERR_TUTORIAL_CREATION_FAILED + " (" + ex.getLocalizedMessage() + ")",
					DialogDictionary.COMMON_BUTTON_OK.toString()).askUserFeedback();
		}
	}

	private String buildStaticPortionOfScript() throws IOException {
		final StringBuilder completeSql = new StringBuilder();
		// Daten für die Beispiele
		completeSql.append(FileUtil.readResourceFile("/sql/examples.sql")).append("\n");
		// Daten für die Übungen
		completeSql.append(FileUtil.readResourceFile("/sql/tutorial.sql")).append("\n");
		return completeSql.toString();
	}

	private String buildDynamicPortionOfScript(final Connection connection) throws IOException, SQLException {
		return new StringBuilder(generateDynamicScript("/sql/second_user.sql", connection)).append("\n")
				.toString();
	}

	/**
	 * Führt ein Skript aus und liest dessen Ausgaben als eine Reihe von
	 * Anweisungen ein ud erzeugt daraus dynamisch ein SQL-Skript.
	 *
	 * @param resourceFilename
	 * @param connection
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private String generateDynamicScript(final String resourceFilename, final Connection connection)
			throws SQLException, IOException {
		final StringBuilder dynamicScript = new StringBuilder();
		if (connection != null) {
			final StringBuilder completeSql = new StringBuilder();
			completeSql.append(FileUtil.readResourceFile(resourceFilename)).append("\n");
			for (final StatementString statement : new StatementExtractor()
					.getStatementsFromScript(completeSql.toString())) {
				for (final ResultRow dynamicStatement : new StatementExecution(statement)
						.execute(connection.createStatement()).getStatementResult().getRows()) {
					for (final String column : dynamicStatement.columnsAsString()) {
						dynamicScript.append(column).append("\n");
					}
				}
			}
		}
		return dynamicScript.toString();
	}
}

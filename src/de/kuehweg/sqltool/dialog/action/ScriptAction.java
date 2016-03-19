/*
 * Copyright (c) 2013-2016, Michael Kühweg
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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.FileUtil;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import de.kuehweg.sqltool.dialog.component.StatementEditorHolder;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * SQL-Skripte laden und speichern.
 *
 * @author Michael Kühweg
 */
public class ScriptAction {

	private final StatementEditorHolder statementEditorHolder;

	private Window windowForFileChooser;

	/**
	 * Quelle und Ziel ist jeweils der aktuelle StatementEditor.
	 *
	 * @param statementEditorHolder
	 */
	public ScriptAction(final StatementEditorHolder statementEditorHolder) {
		this.statementEditorHolder = statementEditorHolder;
	}

	public ScriptAction attachFileChooserToWindow(final Window window) {
		windowForFileChooser = window;
		return this;
	}

	/**
	 * Datei auswählen, laden und in der TextArea anzeigen.
	 */
	public void loadScript() {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(DialogDictionary.LABEL_OPEN_SCRIPT.toString());
		final File file = fileChooser.showOpenDialog(windowForFileChooser);
		if (file != null) {
			try {
				final String script = FileUtil.readFile(FileUtil.convertToURI(file).toURL());
				statementEditorHolder.getActiveStatementEditor().setText(script);
			} catch (final IOException | URISyntaxException ex) {
				final ErrorMessage msg = new ErrorMessage(DialogDictionary.MESSAGEBOX_ERROR.toString(),
						DialogDictionary.ERR_FILE_OPEN_FAILED.toString(), DialogDictionary.COMMON_BUTTON_OK.toString());
				msg.askUserFeedback();
			}
		}
	}

	/**
	 * Inhalt der TextArea als Datei speichern (mit Dateiauswahl).
	 */
	public void saveScript() {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(DialogDictionary.LABEL_SAVE_SCRIPT.toString());
		final File file = fileChooser.showSaveDialog(windowForFileChooser);
		if (file != null) {
			try {
				FileUtil.writeFile(FileUtil.enforceExtension(file.toURI().toURL(), "sql"),
						statementEditorHolder.getActiveStatementEditor().getText());
			} catch (final IOException ex) {
				final ErrorMessage msg = new ErrorMessage(DialogDictionary.MESSAGEBOX_ERROR.toString(),
						DialogDictionary.ERR_FILE_SAVE_FAILED.toString(), DialogDictionary.COMMON_BUTTON_OK.toString());
				msg.askUserFeedback();

			}
		}
	}
}

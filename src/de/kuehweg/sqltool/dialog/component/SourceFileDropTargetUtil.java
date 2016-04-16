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
package de.kuehweg.sqltool.dialog.component;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.FileUtil;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

/**
 * Drag and Drop für SQL-Skripte.
 *
 * @author Michael Kühweg
 */
public class SourceFileDropTargetUtil {

	private static final String DROP_TARGET_SYMBOL_ID = "removeMeOnDragExited";

	private static final double DRAG_EFFECT_OPACITY_NORMAL = 1.0;
	private static final double DRAG_EFFECT_OPACITY_DURING_DRAG = 0.5;

	private static final double DRAG_EFFECT_WIDTH = 5;
	private static final double DRAG_EFFECT_HEIGHT = 5;
	private static final int DRAG_EFFECT_ITERATIONS = 3;

	private SourceFileDropTargetUtil() {
		// Util
	}

	private static void setOnDragOver(final Pane containerPane) {

		containerPane.setOnDragOver(event -> {
			final Dragboard dragboard = event.getDragboard();
			if (event.getGestureSource() != containerPane && dragboard.hasFiles() && dragboard.getFiles().size() == 1) {
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
			event.consume();
		});
	}

	private static void setDragEffect(final Node node) {
		node.setOpacity(DRAG_EFFECT_OPACITY_DURING_DRAG);
		// Blur-Effekt kann im Moment noch nicht in FX-CSS angegeben werden
		final BoxBlur bb = new BoxBlur();
		bb.setWidth(DRAG_EFFECT_WIDTH);
		bb.setHeight(DRAG_EFFECT_HEIGHT);
		bb.setIterations(DRAG_EFFECT_ITERATIONS);
		node.setEffect(bb);
	}

	private static void resetDragEffect(final Node node) {
		node.setOpacity(DRAG_EFFECT_OPACITY_NORMAL);
		node.setEffect(null);
	}

	private static void setOnDragEntered(final Pane containerPane) {
		containerPane.setOnDragEntered(event -> {
			final Dragboard dragboard = event.getDragboard();
			if (event.getGestureSource() != containerPane && dragboard.hasFiles() && dragboard.getFiles().size() == 1) {
				for (final Node node : containerPane.getChildren()) {
					SourceFileDropTargetUtil.setDragEffect(node);
				}
				final Pane dropTargetSymbol = new DropTargetSymbol(containerPane);
				dropTargetSymbol.setId(DROP_TARGET_SYMBOL_ID);
				containerPane.getChildren().add(dropTargetSymbol);
			}
			event.consume();
		});
	}

	private static void setOnDragExited(final Pane containerPane) {
		containerPane.setOnDragExited(event -> {
			for (final Node node : containerPane.getChildren()) {
				SourceFileDropTargetUtil.resetDragEffect(node);
			}
			final Node child = containerPane.lookup("#" + DROP_TARGET_SYMBOL_ID);
			if (child != null) {
				containerPane.getChildren().remove(child);
			}
			event.consume();
		});
	}

	private static void setOnDragDropped(final Pane containerPane,
			final StatementEditorComponentAccessor statementEditorHolder) {
		containerPane.setOnDragDropped(event -> {
			final Dragboard dragboard = event.getDragboard();
			boolean success = false;
			if (dragboard.hasFiles() && dragboard.getFiles().size() == 1) {
				try {
					final String script = FileUtil.readFile(FileUtil.convertToURI(dragboard.getFiles().get(0)).toURL());
					statementEditorHolder.getStatementEditorComponent().getActiveStatementEditor().setText(script);
					success = true;
				} catch (final Exception ex) {
					// egal welche Exception passiert, der Drop muss
					// beendet werden, damit die Anzeige zurückgesetzt wird
					final ErrorMessage msg = new ErrorMessage(DialogDictionary.MESSAGEBOX_ERROR.toString(),
							DialogDictionary.ERR_FILE_OPEN_FAILED.toString(),
							DialogDictionary.COMMON_BUTTON_OK.toString());
					msg.askUserFeedback();
				}
			}
			event.setDropCompleted(success);
			event.consume();
		});
	}

	/**
	 * Macht den angegebenen Container zu einem DropTarget für SQL-Skripte und
	 * setzt die TextArea als Ziel für den Skriptinhalt.
	 *
	 * @param containerPane
	 *            DropTarget
	 * @param statementEditorHolder
	 *            Hier kann das Ziel für das Skript abgefragt werden
	 */
	public static void transformIntoSourceFileDropTarget(final Pane containerPane,
			final StatementEditorComponentAccessor statementEditorHolder) {

		setOnDragOver(containerPane);

		setOnDragEntered(containerPane);

		setOnDragExited(containerPane);

		setOnDragDropped(containerPane, statementEditorHolder);

	}
}

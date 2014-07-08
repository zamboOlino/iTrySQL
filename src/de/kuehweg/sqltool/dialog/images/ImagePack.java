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
package de.kuehweg.sqltool.dialog.images;

import javafx.scene.image.Image;

/**
 * Bilder, Icons,... ansprechbar machen
 * 
 * @author Michael Kühweg
 */
public enum ImagePack {

	APP_ICON("AppIcon.png"),
	TUTORIAL_DATA("tutorialdata.png"),
	TREE_DATABASE("tree_database.png"),
	TREE_SCHEMA("tree_schema.png"),
	TREE_TABLE("tree_table.png"),
	TREE_COLUMN("tree_column.png"),
	TREE_INDEX("tree_index.png"),
	TREE_PRIMARY_KEY("tree_pk.png"),
	TREE_REFERENCES("tree_ref.png"),
	TREE_REFERENCED_BY("tree_ref_by.png"),
	MSG_ERROR("msg_error.png"),
	MSG_INFO("msg_info.png"),
	MSG_QUESTION("msg_question.png"),
	MSG_WARNING("msg_warning.png");
	private static final String resourcePath = "/resources/images/";
	private final String imageName;

	private ImagePack(final String imageName) {
		this.imageName = imageName;
	}

	public Image getAsImage() {
		return new Image(this.getClass().getResourceAsStream(
				resourcePath + imageName));
	}
}

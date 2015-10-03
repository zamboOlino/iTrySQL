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
package de.kuehweg.sqltool.database.formatter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.kuehweg.sqltool.common.FileUtil;

/**
 * Standardvorlage für HTML-Ausgabe.
 *
 * @author Michael Kühweg
 */
public class DefaultHtmlResultTemplate extends ResultTemplate {

	private static final long serialVersionUID = -713360041375851444L;

	private static final String DEFAULT_TEMPLATE_URL = "/resources/templates/exporttemplate_html.txt";

	@Override
	public String getTemplate() {
		if (super.getTemplate() == null) {
			try {
				setTemplate(FileUtil.readResourceFile(DEFAULT_TEMPLATE_URL));
			} catch (final IOException ex) {
				Logger.getLogger(DefaultHtmlResultTemplate.class.getName()).log(Level.SEVERE,
						"Could not read template file for HTML export", ex);
				setTemplate(ex.getLocalizedMessage());
			}
		}
		return super.getTemplate();
	}
}

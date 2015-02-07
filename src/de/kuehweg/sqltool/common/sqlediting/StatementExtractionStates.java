/*
 * Copyright (c) 2013-2015, Michael Kühweg
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
package de.kuehweg.sqltool.common.sqlediting;

/**
 * Mini-Automat um eine auszuführende SQL-Anweisung in einem Eingangstext zu
 * identifizieren. Jeder Status erwartet das aktuell auszuwertende Zeichen. Der
 * Status FINAL zeigt an, dass das Ende einer SQL-Anweisung erreicht wurde. Vom
 * Start eines Textes an, können somit die enthaltenen SQL-Anweisungen separiert
 * werden.
 * 
 * @author Michael Kühweg
 */
public enum StatementExtractionStates {

	/**
	 * Mit diesem Status wird immer begonnen...
	 */
	START {
		@Override
		public StatementExtractionStates evaluate(final char input) {
			if (input == '/') {
				return MAYBE_BLOCK_COMMENT;
			} else if (input == '-') {
				return MAYBE_LINE_COMMENT;
			} else if (input == '\'') {
				return STRING_LITERAL;
			} else if (input == '\"') {
				return QUOTED_NAME;
			} else if (input == ';') {
				return FINAL;
			}
			return START;
		}
	},
	MAYBE_BLOCK_COMMENT {
		@Override
		public StatementExtractionStates evaluate(final char input) {
			if (input == '*') {
				return INSIDE_BLOCK_COMMENT;
			}
			return START;
		}
	},
	INSIDE_BLOCK_COMMENT {
		@Override
		public StatementExtractionStates evaluate(final char input) {
			if (input == '*') {
				return MAYBE_END_OF_BLOCK_COMMENT;
			}
			return INSIDE_BLOCK_COMMENT;
		}
	},
	MAYBE_END_OF_BLOCK_COMMENT {
		@Override
		public StatementExtractionStates evaluate(final char input) {
			if (input == '/') {
				return START;
			}
			return INSIDE_BLOCK_COMMENT;
		}
	},
	MAYBE_LINE_COMMENT {
		@Override
		public StatementExtractionStates evaluate(final char input) {
			if (input == '-') {
				return INSIDE_LINE_COMMENT;
			}
			return START;
		}
	},
	INSIDE_LINE_COMMENT {
		@Override
		public StatementExtractionStates evaluate(final char input) {
			if (input == '\n' || input == '\r') {
				return START;
			}
			return INSIDE_LINE_COMMENT;
		}
	},
	STRING_LITERAL {
		@Override
		public StatementExtractionStates evaluate(final char input) {
			if (input == '\'') {
				return START;
			}
			return STRING_LITERAL;
		}
	},
	QUOTED_NAME {
		@Override
		public StatementExtractionStates evaluate(final char input) {
			if (input == '\"') {
				return START;
			}
			return QUOTED_NAME;
		}
	},
	/**
	 * wenn dieser Status erreicht ist, ist das Ende einer SQL-Anweisung (oder
	 * des Eingabetexts) erreicht.
	 */
	FINAL;

	public StatementExtractionStates evaluate(final char input) {
		throw new IllegalArgumentException(name());
	}
}

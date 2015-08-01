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

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.execution.ResultHeader;
import de.kuehweg.sqltool.database.execution.ResultRow;
import de.kuehweg.sqltool.database.execution.StatementExecutionInformation;
import de.kuehweg.sqltool.database.execution.StatementResult;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Michael Kühweg
 */
public class HtmlResultFormatterTest {

    private StatementExecutionInformation info;

    public HtmlResultFormatterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        info = new StatementExecutionInformation();

        info.setExecutedBy("<ex>");
        info.setConnectionDescription("<connerie>");
        info.setStartOfExecution(new GregorianCalendar(1984, Calendar.JANUARY, 24, 9,
                30, 13).getTimeInMillis());
        info.setEndOfExecution(new GregorianCalendar(1984, Calendar.JANUARY, 24, 9,
                30, 42).getTimeInMillis());
        info.setSummary("<all's well that ends well>");

        StatementResult statementResult = new StatementResult();
        statementResult.setHeader(new ResultHeader("<col1>", "<col2>"));
        statementResult.addRow(new ResultRow(1, "<BCD>"));

        info.setStatementResult(statementResult);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void fullTemplate() {

        ResultTemplate template = new ResultTemplate();
        template.setPlaceholderExecution("E");
        template.setPlaceholderResultTable("R");
        template.setPlaceholderRowCount("C");
        template.setPlaceholderLimitRows("L");

        template.setTemplate("E");
        Assert.assertEquals("[24.01.1984 09:30:13 &lt;ex&gt;@&lt;connerie&gt;]",
                new HtmlResultFormatter(info).format(template));

        template.setTemplate("R");
        String resultHeader
                = "<thead><tr><th>&lt;col1&gt;</th><th>&lt;col2&gt;</th></tr></thead>";
        String resultRow = "<tbody>\n<tr><td>1</td><td>&lt;BCD&gt;</td></tr>\n</tbody>\n";
        String expectedResult = "<table>\n" + resultHeader + resultRow + "</table>\n";
        Assert.
                assertEquals(expectedResult, new HtmlResultFormatter(info).
                        format(template));

        template.setTemplate("C");
        Assert.
                assertEquals(MessageFormat.format(DialogDictionary.PATTERN_ROWCOUNT.
                                toString(), 1), new HtmlResultFormatter(info).
                        format(template));

        info.setLimitMaxRowsReached(true);
        template.setTemplate("L");
        Assert.
                assertEquals(MessageFormat.format(DialogDictionary.PATTERN_MAX_ROWS.
                                toString(), 1), new HtmlResultFormatter(info).
                        format(template));
    }

    @Test
    public void emptyResult() {

        info.setStatementResult(null);

        ResultTemplate template = new ResultTemplate();
        template.setPlaceholderExecution("E");
        template.setPlaceholderResultTable("R");
        template.setPlaceholderRowCount("C");
        template.setPlaceholderLimitRows("L");
        template.setTemplate("E\nR\nC\nL\n");

        final String whenAndWho = "[24.01.1984 09:30:13 &lt;ex&gt;@&lt;connerie&gt;]";

        // summary wird an der Position des rowcount eingesetzt - mit vorgegebenem
        // Template also zwei Leerzeilen zwischen allgemeinen Ausführungsdaten und der
        // Zusammenfassung
        final String expectedResult = whenAndWho + "\n\n"
                + "&lt;all's well that ends well&gt;"
                + "\n\n";
        Assert.
                assertEquals(expectedResult, new HtmlResultFormatter(info).
                        format(template));
    }
}
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
package de.kuehweg.sqltool.database.metadata;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.kuehweg.sqltool.database.execution.fake.ResultSetStubForMetaDataReader;
import de.kuehweg.sqltool.database.metadata.description.CatalogDescription;
import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.description.SchemaDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;

/**
 * Metadaten von Tabellen testen.
 *
 * @author Michael Kühweg
 */
public class TableMetaDataReaderTest {

	private DatabaseDescription db;
	private CatalogDescription catalog;
	private SchemaDescription schema;

	public TableMetaDataReaderTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		db = new DatabaseDescription("db", "product", "version");

		catalog = new CatalogDescription("TABLE_CAT");
		db.adoptOrphan(catalog);

		schema = new SchemaDescription("TABLE_SCHEM");
		catalog.adoptOrphan(schema);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void readMetaData() throws SQLException {
		final TableMetaDataReader metaDataReader = new TableMetaDataReader(db);
		metaDataReader.readAndAddDescriptions(new ResultSetStubForMetaDataReader(2, "TABLE_NAME"));

		assertEquals(2, schema.getTables().size());

		// alle Daten übertragen ?
		int count = 1;
		for (final TableDescription table : schema.getTables()) {
			assertEquals("TABLE_NAME" + count++, table.getName());
			assertEquals("TABLE_TYPE", table.getTableType());
			assertEquals("REMARKS", table.getRemarks());
		}
	}
}

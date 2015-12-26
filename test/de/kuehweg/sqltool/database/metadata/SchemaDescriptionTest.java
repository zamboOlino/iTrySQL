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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.kuehweg.sqltool.database.metadata.description.CatalogDescription;
import de.kuehweg.sqltool.database.metadata.description.DatabaseDescription;
import de.kuehweg.sqltool.database.metadata.description.SchemaDescription;
import de.kuehweg.sqltool.database.metadata.description.TableDescription;

/**
 * @author Michael Kühweg
 */
public class SchemaDescriptionTest {

	public SchemaDescriptionTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void tableTypes() {
		final DatabaseDescription db = new DatabaseDescription("db", "product", "version");

		final CatalogDescription catalog = new CatalogDescription("CATALOG");
		db.adoptOrphan(catalog);

		final SchemaDescription schema = new SchemaDescription("SCHEMA");
		catalog.adoptOrphan(schema);

		final TableDescription table11 = new TableDescription("TABLE-1", "TYPE-1", "REMARKS");
		final TableDescription table21 = new TableDescription("TABLE-2", "TYPE-1", "REMARKS");
		final TableDescription table32 = new TableDescription("TABLE-3", "TYPE-2", "REMARKS");

		schema.adoptOrphan(table11);
		schema.adoptOrphan(table21);
		schema.adoptOrphan(table32);

		assertEquals(2, schema.getTableTypes().size());
		assertEquals(2, schema.getTablesByType("TYPE-1").size());
		assertEquals(1, schema.getTablesByType("TYPE-2").size());
	}

	@Test
	public void nullSafe() {
		assertEquals("", new SchemaDescription(null).getName());
	}
}

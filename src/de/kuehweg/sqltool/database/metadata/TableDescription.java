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
package de.kuehweg.sqltool.database.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Beschreibung der Tabellen-Metadaten.
 *
 * @author Michael Kühweg
 */
public class TableDescription implements Comparable<TableDescription> {

    private final String catalog;
    private final String schema;
    private final String tableName;
    private final String tableType;
    private final String remarks;
    private final Set<ColumnDescription> columns;
    private final Set<IndexDescription> indices;
    private final Set<ForeignKeyColumnDescription> foreignKeys;
    private final Set<ForeignKeyColumnDescription> referencedBy;
    private final Set<PrimaryKeyColumnDescription> primaryKey;

    public TableDescription(final String catalog, final String schema,
            final String tableName, final String tableType, final String remarks) {
        this.catalog = catalog == null ? "" : catalog;
        this.schema = schema == null ? "" : schema;
        this.tableName = tableName == null ? "" : tableName;
        this.tableType = tableType;
        this.remarks = remarks;
        columns = new HashSet<>();
        indices = new HashSet<>();
        foreignKeys = new HashSet<>();
        referencedBy = new HashSet<>();
        primaryKey = new HashSet<>();
    }

    public String getCatalog() {
        return catalog;
    }

    public String getSchema() {
        return schema;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public List<ColumnDescription> getColumns() {
        final List<ColumnDescription> result = new ArrayList<>(columns);
        Collections.sort(result);
        return result;
    }

    public void addColumns(final Collection<ColumnDescription> cols) {
        if (cols != null) {
            for (final ColumnDescription col : cols) {
                columns.add(col);
            }
        }
    }

    public List<IndexDescription> getIndices() {
        final List<IndexDescription> result = new ArrayList<>(indices);
        Collections.sort(result);
        return result;
    }

    public void addIndices(final Collection<IndexDescription> inds) {
        if (inds != null) {
            for (final IndexDescription index : inds) {
                indices.add(index);
            }
        }
    }

    public List<ForeignKeyColumnDescription> getForeignKeys() {
        final List<ForeignKeyColumnDescription> result = new ArrayList<>(foreignKeys);
        Collections.sort(result);
        return result;
    }

    public void addForeignKeys(final Collection<ForeignKeyColumnDescription> fks) {
        if (fks != null) {
            for (final ForeignKeyColumnDescription fk : fks) {
                foreignKeys.add(fk);
            }
        }
    }

    public List<ForeignKeyColumnDescription> getReferencedBy() {
        final List<ForeignKeyColumnDescription> result = new ArrayList<>(referencedBy);
        Collections.sort(result);
        return result;
    }

    public void addReferencedBy(final Collection<ForeignKeyColumnDescription> refs) {
        if (refs != null) {
            for (final ForeignKeyColumnDescription ref : refs) {
                referencedBy.add(ref);
            }
        }
    }

    public Set<PrimaryKeyColumnDescription> getPrimaryKey() {
        return primaryKey;
    }

    public void addPrimaryKeys(final Collection<PrimaryKeyColumnDescription> primaryKeyColumns) {
        if (primaryKeyColumns != null) {
            for (final PrimaryKeyColumnDescription pk : primaryKeyColumns) {
                primaryKey.add(pk);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(catalog);
        hash = 97 * hash + Objects.hashCode(schema);
        hash = 97 * hash + Objects.hashCode(tableName);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TableDescription other = (TableDescription) obj;
        if (!Objects.equals(catalog, other.catalog)) {
            return false;
        }
        if (!Objects.equals(schema, other.schema)) {
            return false;
        }
        return Objects.equals(tableName, other.tableName);
    }

    @Override
    public int compareTo(final TableDescription other) {
        int result = catalog.compareTo(other.catalog);
        if (result == 0) {
            result = schema.compareTo(other.schema);
        }
        if (result == 0) {
            result = tableName.compareTo(other.tableName);
        }
        return result;
    }
}

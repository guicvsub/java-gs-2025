-- V2__Add_Operador_Id_To_Transacoes.sql
-- Adiciona a coluna operador_id se ela não existir (MySQL não suporta IF NOT EXISTS)

-- Verifica e adiciona a coluna operador_id se não existir
SET @dbname = DATABASE();
SET @tablename = "transacoes";
SET @columnname = "operador_id";
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  "SELECT 'Column already exists.'",
  CONCAT("ALTER TABLE ", @tablename, " ADD COLUMN ", @columnname, " BIGINT")
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Adiciona foreign key se não existir
SET @fk_name = "fk_transacao_operador";
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (constraint_name = @fk_name)
  ) > 0,
  "SELECT 'Foreign key already exists.'",
  CONCAT("ALTER TABLE ", @tablename, " ADD CONSTRAINT ", @fk_name, " FOREIGN KEY (operador_id) REFERENCES operadores(id) ON DELETE SET NULL")
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Adiciona índice se não existir
SET @index_name = "idx_operador_id";
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (index_name = @index_name)
  ) > 0,
  "SELECT 'Index already exists.'",
  CONCAT("ALTER TABLE ", @tablename, " ADD INDEX ", @index_name, " (operador_id)")
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;


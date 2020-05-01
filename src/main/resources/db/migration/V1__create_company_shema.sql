CREATE TABLE companies
(
    id      INT         NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE,
    uuid    BIGINT      NOT NULL,
    UNIQUE INDEX uuid_UNIQUE (uuid ASC) VISIBLE,
    name    VARCHAR(55) NOT NULL,
    UNIQUE INDEX name_UNIQUE (name ASC) VISIBLE,
    address VARCHAR(55) NOT NULL
);
CREATE TABLE stores
(
    id               INT         NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE,
    name             VARCHAR(55) NOT NULL,
    UNIQUE INDEX name_UNIQUE (name ASC) VISIBLE,
    address          VARCHAR(55) NOT NULL,
    fk_store_company INT         NULL DEFAULT NULL,
    INDEX fk_store_company_idx (fk_store_company ASC) VISIBLE,
    CONSTRAINT fk_store_company FOREIGN KEY (fk_store_company) REFERENCES companies (id) ON DELETE SET NULL ON UPDATE NO ACTION
);
CREATE TABLE customers
(
    id      INT         NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE,
    uuid    BIGINT      NOT NULL,
    UNIQUE INDEX uuid_UNIQUE (uuid ASC) VISIBLE,
    name    VARCHAR(55) NOT NULL,
    address VARCHAR(55) NULL
);
CREATE TABLE cards
(
    id          INT         NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE,
    number      BIGINT      NOT NULL,
    UNIQUE INDEX number_UNIQUE (number ASC) VISIBLE,
    type        VARCHAR(55) NOT NULL,
    contactless TINYINT     NULL
);
CREATE TABLE receipts
(
    id               INT         NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE,
    total            DOUBLE      NOT NULL,
    date_time        TIMESTAMP   NOT NULL,
    payment_type     VARCHAR(55) NOT NULL,
    fk_receipt_store INT         NULL DEFAULT NULL,
    INDEX fk_receipt_store_idx (fk_receipt_store ASC) VISIBLE,
    CONSTRAINT fk_receipt_store FOREIGN KEY (fk_receipt_store) REFERENCES stores (id) ON DELETE SET NULL ON UPDATE NO ACTION,
    fk_receipt_card  INT         NULL DEFAULT NULL,
    INDEX fk_receipt_card_idx (fk_receipt_card ASC) VISIBLE,
    CONSTRAINT fk_receipt_card FOREIGN KEY (fk_receipt_card) REFERENCES cards (id) ON DELETE SET NULL ON UPDATE NO ACTION
);
CREATE TABLE invoices
(
    id                  INT         NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE,
    total               DOUBLE      NOT NULL,
    date_time           TIMESTAMP   NOT NULL,
    payment_type        VARCHAR(55) NOT NULL,
    fk_invoice_store    INT         NULL DEFAULT NULL,
    INDEX fk_invoice_store_idx (fk_invoice_store ASC) VISIBLE,
    CONSTRAINT fk_invoice_store FOREIGN KEY (fk_invoice_store) REFERENCES stores (id) ON DELETE SET NULL ON UPDATE NO ACTION,
    fk_invoice_card     INT         NULL DEFAULT NULL,
    INDEX fk_invoice_card_idx (fk_invoice_card ASC) VISIBLE,
    CONSTRAINT fk_invoice_card FOREIGN KEY (fk_invoice_card) REFERENCES cards (id) ON DELETE SET NULL ON UPDATE NO ACTION,
    fk_invoice_customer INT         NULL DEFAULT NULL,
    INDEX fk_invoice_customer_idx (fk_invoice_customer ASC) VISIBLE,
    CONSTRAINT fk_invoice_customer FOREIGN KEY (fk_invoice_customer) REFERENCES customers (id) ON DELETE SET NULL ON UPDATE NO ACTION
);

CREATE TABLE `new_schema`.`parsed_xml_files`
(
    `id`        INT         NOT NULL AUTO_INCREMENT,
    `filename`  VARCHAR(45) NOT NULL,
    `date_time` TIMESTAMP   NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `filename_UNIQUE` (`filename` ASC) VISIBLE
);

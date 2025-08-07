CREATE TABLE IF NOT EXISTS companies (
  id BIGINT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL,
  company_name VARCHAR(255) NOT NULL,
  license_number VARCHAR(255) NOT NULL,
  boss VARCHAR(255) NOT NULL,
  role ENUM('COMPANY', 'USER') NULL,
  deleted BIT(1) NOT NULL,
  created_at DATETIME(6),
  updated_at DATETIME(6),
  PRIMARY KEY (id),
  UNIQUE INDEX UK_email (email),
  UNIQUE INDEX UK_license_number (license_number)
);

CREATE TABLE IF NOT EXISTS product (
  id BIGINT NOT NULL AUTO_INCREMENT,
  code VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  price INT NOT NULL,
  img_url VARCHAR(255) NOT NULL,
  company_id BIGINT NOT NULL,
  deleted BIT(1) NOT NULL,
  created_at DATETIME(6),
  updated_at DATETIME(6),
  PRIMARY KEY (id),
  UNIQUE INDEX UK_product_code (code),
  INDEX FK_product_company (company_id),
  CONSTRAINT FK_product_company FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  phone VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL,
  role ENUM('COMPANY', 'USER') NOT NULL,
  deleted BIT(1) NOT NULL,
  created_at DATETIME(6),
  updated_at DATETIME(6),
  PRIMARY KEY (id),
  UNIQUE INDEX UK_user_email (email)
);

CREATE TABLE IF NOT EXISTS orders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  total_price INT NOT NULL,
  order_status ENUM('CANCELLED', 'PAID', 'PENDING') NOT NULL,
  created_at DATETIME(6),
  updated_at DATETIME(6),
  PRIMARY KEY (id),
  INDEX FK_orders_user (user_id),
  CONSTRAINT FK_orders_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS order_item (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT,
  product_id BIGINT,
  quantity INT NOT NULL,
  total_price INT NOT NULL,
  created_at DATETIME(6),
  updated_at DATETIME(6),
  PRIMARY KEY (id),
  INDEX FK_item_order (order_id),
  INDEX FK_item_product (product_id),
  CONSTRAINT FK_item_order FOREIGN KEY (order_id) REFERENCES orders (id),
  CONSTRAINT FK_item_product FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE IF NOT EXISTS payments (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  amount INT NOT NULL,
  payment_status ENUM('CANCEL', 'PAID', 'READY') NOT NULL,
  paid_at DATETIME(6),
  created_at DATETIME(6),
  updated_at DATETIME(6),
  PRIMARY KEY (id),
  UNIQUE INDEX UK_payment_order (order_id),
  CONSTRAINT FK_payment_order FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE IF NOT EXISTS stock (
  id BIGINT NOT NULL AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  created_at DATETIME(6),
  updated_at DATETIME(6),
  PRIMARY KEY (id),
  UNIQUE INDEX UK_stock_product (product_id),
  CONSTRAINT FK_stock_product FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE IF NOT EXISTS stock_history (
  id BIGINT NOT NULL AUTO_INCREMENT,
  stock_id BIGINT NOT NULL,
  quantity_change INT NOT NULL,
  stock_status ENUM('INBOUND', 'OUTBOUND', 'SOLD_OUT', 'STOCK_DECREASED', 'STOCK_INCREASED') NOT NULL,
  created_at DATETIME(6),
  updated_at DATETIME(6),
  PRIMARY KEY (id),
  INDEX FK_history_stock (stock_id),
  CONSTRAINT FK_history_stock FOREIGN KEY (stock_id) REFERENCES stock (id)
);
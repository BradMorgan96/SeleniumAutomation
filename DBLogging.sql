CREATE TABLE SeleniumAutomationLog(
  entry_id INT(10) AUTO_INCREMENT,
  class_name VARCHAR(75) NOT NULL,
  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  environment VARCHAR(75) NOT NULL,
  run_start VARCHAR(75),
  log_time VARCHAR(75),
  message VARCHAR(9999),
  PRIMARY KEY(entry_id)
);

SELECT * FROM SeleniumAutomationLog ORDER BY entry_id DESC;
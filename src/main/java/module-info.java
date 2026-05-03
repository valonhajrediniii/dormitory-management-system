module com.dormitory.management {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive java.sql;
    requires org.postgresql.jdbc;

    opens com.dormitory.management.controller to javafx.fxml;
    exports com.dormitory.management;
    exports com.dormitory.management.model;
    exports com.dormitory.management.service;
    exports com.dormitory.management.dao;
}

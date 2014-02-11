mvn clean
mvn assembly:assembly


scp target/PAIS*-dependencies.jar db2inst1@pais1.cci.emory.edu:/shared/database/db2inst1/alex/javaTools

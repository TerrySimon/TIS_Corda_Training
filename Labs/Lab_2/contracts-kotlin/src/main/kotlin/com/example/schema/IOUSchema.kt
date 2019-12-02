package com.example.schema

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * The family of schemas for IOUState.
 */
object IOUSchema

/**
 * An IOUState schema.
 * A single JPA entity called PersistentIOU
 * This class must extend the MappedSchema class. Its name is based on the SchemaFamily name and the associated version number abbreviation (V1, V2... Vn).
 * In the constructor, call the constructor of MappedSchema with the following arguments:
 *   - a class literal representing the schema family,
 *   - a version number and
 *   - a collection of mappedTypes (class literals) which represent JPA entity classes that the ORM layer needs to be configured with for this schema
 */
object IOUSchemaV1 : MappedSchema(schemaFamily = IOUSchema.javaClass, version = 1, mappedTypes = listOf(PersistentIOU::class.java)) {
    /**
     * The @Entity annotation signifies that the specified POJO class' non-transient fields should be persisted to a relational database using the services
     * of an entity manager.
     * The @Table annotation specifies properties of the table that will be created to contain the persisted data, in this case we have specified a name argument
     * which will be used the table's title.
     */
    @Entity
    @Table(name = "iou_states")
    class PersistentIOU(
            /**
             * The @Column annotations specify the columns that will comprise the inserted table and specify the shape of the fields and associated
             * data types of each database entry.
             */
            @Column(name = "lender")
            var lenderName: String,

            @Column(name = "borrower")
            var borrowerName: String,

            @Column(name = "value")
            var value: Int,

            @Column(name = "linear_id")
            var linearId: UUID
    ) : PersistentState() {
        // Default constructor required by hibernate.
        constructor(): this("", "", 0, UUID.randomUUID())
    }
}
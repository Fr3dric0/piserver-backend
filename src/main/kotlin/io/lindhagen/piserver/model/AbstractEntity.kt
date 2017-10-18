package io.lindhagen.piserver.model

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    18.10.2017
 */
@MappedSuperclass
abstract class AbstractEntity (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = -1
)
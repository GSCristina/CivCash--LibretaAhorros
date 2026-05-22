package org.libretaahorros.libretaahorros.model;
/**
 * Enumeración que define las categorías preestablecidas para la clasificación de movimientos financieros.
 * <p>
 * Centraliza el catálogo cerrado de conceptos de imputación de costes e ingresos permitidos
 * en el sistema. Su uso restringe los valores válidos en la capa de persistencia y en la
 * interfaz gráfica, garantizando la integridad de los datos y facilitando los procesos
 * posteriores de filtrado y agregación contable.
 */
public enum Categoria {
    SUPERMERCADO,
    OCIO,
    VIVIENDA,
    TRANSPORTE,
    OTROS,
    IMPUESTOS,
    SUSCRIPCIONES
}

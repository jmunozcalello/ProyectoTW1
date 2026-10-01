INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO Mueble(id, nombre, descripcion, precio, estilo) VALUES
  (1, 'Sillón Retró', 'Sillón de voluteadas tapizado en terciopelo color mostaza, con patas de madera maciza.', 85000.0, 'Retro'),
  (2, 'Lámpara de Pie Retro', 'Lámpara de pie con pantalla de tela y base de metal esmaltado, para rincón de lectura.', 25000.0, 'Retro'),
  (3, 'Escritorio Minimalista', 'Escritorio de melamina clara con cajones y un solo cajón para documentos.', 120000.0, 'Minimalista'),
  (4, 'Biblioteca Minimalista', 'Biblioteca abierta de cinco estantes en madera clara, ideal para pocos libros.', 95000.0, 'Minimalista'),
  (5, 'Mesa de Comedor Japandi', 'Mesa de comedor con tablero de madera maciza y patas cónicas de línea simple.', 150000.0, 'Japandi'),
  (6, 'Sillón Japandi', 'Sillón bajo con cojines de lino natural y estructura de madera clara.', 60000.0, 'Japandi'),
  (7, 'Estantería Industrial', 'Estantería de hierro y madera con estantes reforzados, estilo fábrica.', 75000.0, 'Industrial'),
  (8, 'Cama Boho', 'Cama doble con dosel de telas livianas y estructura de madera clara.', 180000.0, 'Boho'),
  (9, 'Escritorio Escandinavo', 'Escritorio de líneas escandinavas, con terminación clara y tiradores redondos.', 110000.0, 'Escandinavo');

INSERT INTO CategoriaDeMueble(id, nombre, anchoPromedio, profundidadPromedio) VALUES
  (1, 'Biblioteca', 0.80, 0.35),
  (2, 'Cama doble', 1.40, 1.90),
  (3, 'Cama individual', 0.90, 1.90),
  (4, 'Cama queen', 1.60, 2.00),
  (5, 'Escritorio', 1.20, 0.60),
  (6, 'Mesa de comedor', 1.60, 0.90),
  (7, 'Mesa de luz', 0.50, 0.40),
  (8, 'Mesa ratona', 1.10, 0.60),
  (9, 'Placard', 1.80, 0.60),
  (10, 'Rack de TV', 1.60, 0.40),
  (11, 'Silla', 0.45, 0.50),
  (12, 'Sillón', 0.80, 0.85),
  (13, 'Sofá 3 cuerpos', 2.10, 0.90);

CREATE DATABASE IF NOT EXISTS testTPP
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE testTPP;

CREATE TABLE IF NOT EXISTS materia (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    CONSTRAINT uq_materia_codigo UNIQUE (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS establecimiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    localidad VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS alumno (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL,
    email VARCHAR(150) NULL,
    telefono VARCHAR(50) NULL,
    legajo VARCHAR(50) NULL,
    CONSTRAINT uq_alumno_dni UNIQUE (dni),
    CONSTRAINT uq_alumno_legajo UNIQUE (legajo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS ayudante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL,
    email VARCHAR(150) NULL,
    telefono VARCHAR(50) NULL,
    area VARCHAR(100) NULL,
    alumno_vinculado_id INT NULL,
    CONSTRAINT uq_ayudante_dni UNIQUE (dni),
    CONSTRAINT fk_ayudante_alumno FOREIGN KEY (alumno_vinculado_id)
        REFERENCES alumno(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS comision (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    materia_id INT NOT NULL,
    establecimiento_id INT NULL,
    CONSTRAINT uq_comision_materia_nombre UNIQUE (materia_id, nombre),
    CONSTRAINT fk_comision_materia FOREIGN KEY (materia_id)
        REFERENCES materia(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_comision_establecimiento FOREIGN KEY (establecimiento_id)
        REFERENCES establecimiento(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS comision_alumno (
    comision_id INT NOT NULL,
    alumno_id INT NOT NULL,
    PRIMARY KEY (comision_id, alumno_id),
    CONSTRAINT fk_ca_comision FOREIGN KEY (comision_id)
        REFERENCES comision(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_ca_alumno FOREIGN KEY (alumno_id)
        REFERENCES alumno(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS comision_ayudante (
    comision_id INT NOT NULL,
    ayudante_id INT NOT NULL,
    PRIMARY KEY (comision_id, ayudante_id),
    CONSTRAINT fk_cay_comision FOREIGN KEY (comision_id)
        REFERENCES comision(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_cay_ayudante FOREIGN KEY (ayudante_id)
        REFERENCES ayudante(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS horario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dia ENUM('Lunes','Martes','Miercoles','Jueves','Viernes','Sabado','Domingo') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    establecimiento_id INT NULL,
    comision_id INT NOT NULL,
    CONSTRAINT ck_horario_rango CHECK (hora_inicio < hora_fin),
    CONSTRAINT fk_horario_establecimiento FOREIGN KEY (establecimiento_id)
        REFERENCES establecimiento(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_horario_comision FOREIGN KEY (comision_id)
        REFERENCES comision(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS clase (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion VARCHAR(500) NULL,
    comision_id INT NOT NULL,
    tema VARCHAR(200) NULL,
    CONSTRAINT fk_clase_comision FOREIGN KEY (comision_id)
        REFERENCES comision(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS material (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500) NULL,
    llevado BOOLEAN NOT NULL DEFAULT FALSE,
    clase_id INT NULL,
    CONSTRAINT fk_material_clase FOREIGN KEY (clase_id)
        REFERENCES clase(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS asistencia (
    clase_id INT NOT NULL,
    alumno_id INT NOT NULL,
    estado ENUM('SIN_REGISTRAR','PRESENTE','AUSENTE','TARDE','JUSTIFICADA')
        NOT NULL DEFAULT 'SIN_REGISTRAR',
    observacion VARCHAR(500) NULL,
    PRIMARY KEY (clase_id, alumno_id),
    CONSTRAINT fk_asistencia_clase FOREIGN KEY (clase_id)
        REFERENCES clase(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_asistencia_alumno FOREIGN KEY (alumno_id)
        REFERENCES alumno(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
github.com/volta2016/resumen-estudios-git/blob/master/resumen estudio.txt
CREATE TABLE IF NOT EXISTS recordatorio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion VARCHAR(500) NULL,
    comision_id INT NULL,
    clase_id INT NULL,
    material_id INT NULL,
    realizado BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_recordatorio_comision FOREIGN KEY (comision_id)
        REFERENCES comision(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_recordatorio_clase FOREIGN KEY (clase_id)
        REFERENCES clase(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_recordatorio_material FOREIGN KEY (material_id)
        REFERENCES material(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
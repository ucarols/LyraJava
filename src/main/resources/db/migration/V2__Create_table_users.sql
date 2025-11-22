CREATE TABLE tb_users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    humor VARCHAR(20) NULL,
    humor_descricao VARCHAR(500) NULL
);

CREATE TABLE tb_user_roles (
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES tb_roles(id) ON DELETE CASCADE
);

INSERT INTO tb_users (first_name, last_name, email, password)
VALUES ('Fiap', 'Junior', 'fiap@example.com', '123456');

package com.example.lyra.model;

public enum EHumor {
    OTIMO(1, "Ótimo"),
    BEM(2, "Bem"),
    NEUTRO(3, "Neutro"),
    NAO_TAO_BEM(4, "Não tão bem"),
    TERRIVEL(5, "Terrível");

    private final int codigo;
    private final String descricao;

    EHumor(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EHumor fromCodigo(int codigo) {
        for (EHumor humor : EHumor.values()) {
            if (humor.getCodigo() == codigo) {
                return humor;
            }
        }
        throw new IllegalArgumentException("Código de humor inválido: " + codigo);
    }
}

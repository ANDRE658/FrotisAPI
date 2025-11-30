package br.unipar.projetointegrador.frotisapi.dto;

public class DashboardStatsDTO {
    private long ativos;
    private long inativos;
    private long novos;

    public DashboardStatsDTO(long ativos, long inativos, long novos) {
        this.ativos = ativos;
        this.inativos = inativos;
        this.novos = novos;
    }

    // Getters e Setters
    public long getAtivos() { return ativos; }
    public void setAtivos(long ativos) { this.ativos = ativos; }
    public long getInativos() { return inativos; }
    public void setInativos(long inativos) { this.inativos = inativos; }
    public long getNovos() { return novos; }
    public void setNovos(long novos) { this.novos = novos; }
}
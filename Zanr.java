package PozoristeAplikacija;

public enum Zanr {
    komedija(0),
    farsa(1),
    satira(2),
    krestauracije(3),
    tragedija(4),
    istorija(5),
    mjuzikl(6);

    int rbr;
    Zanr(int rbr) {
        this.rbr = rbr;
    }

    public static Zanr izBroj(int rbr) {
        if(rbr == 0)
            return komedija;
        else if(rbr == 1)
            return farsa;
        else if(rbr == 2)
            return satira;
        else if(rbr == 3)
            return krestauracije;
        else if(rbr == 4)
            return tragedija;
        else if(rbr == 5)
            return istorija;
        else if(rbr == 6)
            return mjuzikl;
        else
            throw new IllegalArgumentException();
    }

    public String toString() {
        switch(this.rbr){
            case 0 : return "комедија";
            case 1 : return "фарса";
            case 2 : return "сатира";
            case 3 : return "комедија рестаурације";
            case 4 : return "трагедија";
            case 5 : return "историја";
            case 6 : return "мјузикл";
            default : return "остало";
        }
    }
}

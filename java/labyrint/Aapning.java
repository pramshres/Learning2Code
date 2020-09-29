class Aapning extends HvitRute {

  public Aapning(int rad, int kolonne) {
    super(rad, kolonne);
  }

  @Override
  public char tilTegn() {
    return '.';
  }

  @Override //Metoden sjekkAapning overrider metoden sjekkAapning i rute (returner true hvis ruten er en åpning)
  public boolean sjekkAapning() {
    return true;
  }



}

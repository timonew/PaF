package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;
/** Model-Klasse, deren Objekte (DTO) der Server per WebSocket von den verbundenen Clients empfaengt.
 *  Der Name HelloMessage ist missverstaendlich, da nur der Name einer Person hinterlegt wird, an die
 *  ueber Greeting im Anschluss eine Begruessung gesendet wird. Besser ware ein Bezeichner wie NameDTO.
 */
public class HelloMessage {

  private String name;

  public HelloMessage() {
  }

  public HelloMessage(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

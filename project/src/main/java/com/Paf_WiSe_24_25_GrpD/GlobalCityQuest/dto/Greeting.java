package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.dto;
/** Model-Klasse, deren Objekte (DTO) der Server per WebSocket an die verbundenen Clients schickt.
 */
public class Greeting {

  private String content;

  public Greeting() {
  }

  public Greeting(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

}

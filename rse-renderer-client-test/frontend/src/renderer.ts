import {css, customElement, html, LitElement, property} from 'lit-element';
import '@vaadin/vaadin-text-field';
import { Person } from './Person';

/**
 * Tasks: 
 * *This class should be templated or take a function call back in place of updateDataElement and loadData.
 * *Add documentation as required.
 * *Remove console debug statements
 * *This is just an example so the rest doesn't matter much, (the rest being changing the render() function, etc.).
 */
@customElement("renderer-template")
class Renderer extends LitElement {
  
  /**
   * This is the default data value. It is the empty string and indicates that the JSON has not yet been received from the server.
   */
  person = new Person();

  @property({type: String})
  dataElementJSON = '';

  static styles = css`
    h1 {
      color: blue;
      text-transform: uppercase;
    }
  `;

  constructor() {
    super();
    console.log("Constructed");
  }

  updateFirstName(e: {target: HTMLInputElement}) {
    // First, the event data needs to be read into the local structure
    this.person.firstName = e.target.value;
    // Second, dump into the JSON property
    this.dataElementJSON = JSON.stringify(this.person);
    // Notify the listeners of the change.
    this.dispatchEvent(new CustomEvent(
      'data-changed',
      {bubbles: true, composed: true, detail: e.target.value}
    ));
  }
  updateLastName(e: {target: HTMLInputElement}) {
    this.person.lastName = e.target.value;
    this.dataElementJSON = JSON.stringify(this.person);
    this.dispatchEvent(new CustomEvent(
      'data-changed',
      {bubbles: true, composed: true, detail: e.target.value}
    ));
  }

  loadData() {
    this.person = <Person> JSON.parse(this.dataElementJSON);
    console.log("dEJ-ld = ");
    console.log(this.dataElementJSON);
    console.log("dataElement-ld = ")
    console.log(this.person);
    console.log(this.person.firstName, this.person.lastName);
  }

  render() {
    // Might clean this up a bit - remove some of the <h2> statements.

    return html`
      <script>${this.loadData()}</script>
      <h1>Greetings ${this.person.firstName}!</h1>
      
      <div>
        <vaadin-text-field 
          label="First Name" 
          .value=${this.person.firstName}
          @input=${this.updateFirstName}
          ></vaadin-text-field>
        <vaadin-text-field 
          label="Last Name" 
          .value=${this.person.lastName}
          @input=${this.updateLastName}
          ></vaadin-text-field>
      </div>

      <pre>${JSON.stringify(this.person, null, 2)}</pre>
      
      <h2>${this.person.description}</h2>
      <h2>${this.person.name}</h2>
    `;
  }

}
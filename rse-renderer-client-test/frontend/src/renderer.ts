import {css, customElement, html, LitElement, property} from 'lit-element';
import '@vaadin/vaadin-text-field';

export class DataElement<T> {
     name: string;
     description: string;
     data!: T;
 
     constructor() {
         this.name = 'name';
         this.description = 'description';
     }    
}

@customElement("renderer-template")
class Renderer extends LitElement {
  
  /**
   * This is the default data value. It is the empty string and indicates that the JSON has not yet been received from the server.
   */
  dataElement = new DataElement<string>();

  @property({type: String})
  dataElementJSON = '';

  static styles = css`
    h1 {
      color: hotpink;
      text-transform: uppercase;
    }
  `;

  constructor() {
    super();
    console.log("Constructed");
  }

  /**
   * This operation updates the content of the template from the contents of the data element.
   */
  updateDataElement(e: {target: HTMLInputElement}) {

    console.log("dataElementJSON on update call = " + this.dataElementJSON);
    console.log("dataElement on update call = " + this.dataElement);
  
    // First, the event data needs to be read into the local structure
    this.dataElement.data = e.target.value;
    // Second, dump into the JSON property
    this.dataElementJSON = JSON.stringify(this.dataElement);
    console.log("dataElementJSON = " + this.dataElementJSON);

    // Notify the listeners of the change.
    this.dispatchEvent(new CustomEvent('data-changed', {bubbles: true, composed: true, detail: e.target.value}))

    return;
  }

  loadData() {
    this.dataElement = <DataElement<string>> JSON.parse(this.dataElementJSON);
    console.log("dEJ-ld = ");
    console.log(this.dataElementJSON);
    console.log("dataElement-ld = ")
    console.log(this.dataElement);
    console.log(this.dataElement.data);
  }

  render() {  
    return html`
      <script>${this.loadData()}</script>
      <h1>Greetings ${this.dataElement.name}!</h1>
      
      <div>
        <vaadin-text-field 
          label="Name" 
          .value=${this.dataElement.data}
          @input=${this.updateDataElement}
          ></vaadin-text-field>
      </div>
      
      <h2>${this.dataElementJSON}</h2>
      
      <h2>${this.dataElement.description}</h2>
      <h2>${this.dataElement.name}</h2>
    `;
  }

}
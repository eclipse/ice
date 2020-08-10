/**
 * Person DataElement
 */

import { DataElement } from "./data-element";

export class Person extends DataElement {
	age: number;
	firstName: string;
	lastName: string;

	constructor() {
		super();
		this.age = -1;
		this.firstName = "";
		this.lastName = "";
	}
}
/**
 * Person DataElement.
 * In the future, we hope to be able to generate data structures like this
 * directly from DataElement Spec classes.
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
/**
 * Tasks:
 * 
 * *Move DataElement<T> to its ts file. It would be really nice to put this in the data structures project since it shadows a class there.
 * *Add a unit test?
 */

export class DataElement<T> {
    name: string;
    description: string;
    data!: T;

    constructor() {
        this.name = 'name';
        this.description = 'description';
    }    
}
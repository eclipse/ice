/**
 * Tasks:
 * 
 * *Move DataElement<T> to its ts file. It would be really nice to put this in the data structures project since it shadows a class there.
 * *Add a unit test?
 */
import {v4 as uuidv4} from 'uuid';

export class DataElement {
    privateId: string;
    id: number;
    name: string;
    description: string;
    comment: string;
    context: string;
    required: boolean;
    secret: boolean;

    constructor() {
        this.privateId = uuidv4();
        this.id = 0;
        this.name = 'name';
        this.description = 'description';
        this.comment = 'comment';
        this.context = 'context';
        this.required = false;
        this.secret = true;
    }
}
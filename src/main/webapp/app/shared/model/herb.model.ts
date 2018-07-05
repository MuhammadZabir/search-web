export interface IHerb {
    id?: number;
    name?: string;
    description?: string;
}

export class Herb implements IHerb {
    constructor(public id?: number, public name?: string, public description?: string) {}
}

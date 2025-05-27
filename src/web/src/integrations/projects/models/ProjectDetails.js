// class StageProductProperty {
//     constructor({name, value}) {
//         this.name = name
//         this.value = value
//     }
// }


export class StageProduct {
    constructor({id, name, position, quantity, attributes}) {
        this.id = id
        this.name = name
        this.position = position
        this.quantity = quantity
        this.attributes = attributes
    }
}


export class Stage {
    constructor({id, name, products}) {
        this.id = id
        this.name = name
        this.products = products
    }
}


export class ProjectDetails {
    constructor({id, name, stages}) {
        this.id = id
        this.name = name
        this.stages = stages
    }
}

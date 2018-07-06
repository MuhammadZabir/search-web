import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Herb } from '../../herb/model/herb.model';

@Component({
    selector: 'jhi-herb-view',
    templateUrl: './herb-view.component.html'
})
export class HerbViewComponent implements OnInit {
    herb: Herb;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ herb }) => {
            this.herb = herb;
        });
    }

    previousState() {
        window.history.back();
    }
}

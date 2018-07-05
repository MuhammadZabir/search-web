import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHerb } from 'app/shared/model/herb.model';

@Component({
    selector: 'jhi-herb-detail',
    templateUrl: './herb-detail.component.html'
})
export class HerbDetailComponent implements OnInit {
    herb: IHerb;

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

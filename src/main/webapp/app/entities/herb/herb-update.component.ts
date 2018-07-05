import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IHerb } from 'app/shared/model/herb.model';
import { HerbService } from './herb.service';

@Component({
    selector: 'jhi-herb-update',
    templateUrl: './herb-update.component.html'
})
export class HerbUpdateComponent implements OnInit {
    private _herb: IHerb;
    isSaving: boolean;

    constructor(private herbService: HerbService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ herb }) => {
            this.herb = herb;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.herb.id !== undefined) {
            this.subscribeToSaveResponse(this.herbService.update(this.herb));
        } else {
            this.subscribeToSaveResponse(this.herbService.create(this.herb));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IHerb>>) {
        result.subscribe((res: HttpResponse<IHerb>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get herb() {
        return this._herb;
    }

    set herb(herb: IHerb) {
        this._herb = herb;
    }
}

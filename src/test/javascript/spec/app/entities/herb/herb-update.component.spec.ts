/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { SearchWebTestModule } from '../../../test.module';
import { HerbUpdateComponent } from 'app/entities/herb/herb-update.component';
import { HerbService } from 'app/entities/herb/herb.service';
import { Herb } from 'app/shared/model/herb.model';

describe('Component Tests', () => {
    describe('Herb Management Update Component', () => {
        let comp: HerbUpdateComponent;
        let fixture: ComponentFixture<HerbUpdateComponent>;
        let service: HerbService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [SearchWebTestModule],
                declarations: [HerbUpdateComponent]
            })
                .overrideTemplate(HerbUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HerbUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HerbService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Herb(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.herb = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Herb();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.herb = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});

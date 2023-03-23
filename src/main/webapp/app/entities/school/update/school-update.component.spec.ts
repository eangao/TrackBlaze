import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SchoolFormService } from './school-form.service';
import { SchoolService } from '../service/school.service';
import { ISchool } from '../school.model';

import { SchoolUpdateComponent } from './school-update.component';

describe('School Management Update Component', () => {
  let comp: SchoolUpdateComponent;
  let fixture: ComponentFixture<SchoolUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let schoolFormService: SchoolFormService;
  let schoolService: SchoolService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SchoolUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SchoolUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SchoolUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    schoolFormService = TestBed.inject(SchoolFormService);
    schoolService = TestBed.inject(SchoolService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const school: ISchool = { id: 456 };

      activatedRoute.data = of({ school });
      comp.ngOnInit();

      expect(comp.school).toEqual(school);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchool>>();
      const school = { id: 123 };
      jest.spyOn(schoolFormService, 'getSchool').mockReturnValue(school);
      jest.spyOn(schoolService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ school });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: school }));
      saveSubject.complete();

      // THEN
      expect(schoolFormService.getSchool).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(schoolService.update).toHaveBeenCalledWith(expect.objectContaining(school));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchool>>();
      const school = { id: 123 };
      jest.spyOn(schoolFormService, 'getSchool').mockReturnValue({ id: null });
      jest.spyOn(schoolService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ school: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: school }));
      saveSubject.complete();

      // THEN
      expect(schoolFormService.getSchool).toHaveBeenCalled();
      expect(schoolService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISchool>>();
      const school = { id: 123 };
      jest.spyOn(schoolService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ school });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(schoolService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

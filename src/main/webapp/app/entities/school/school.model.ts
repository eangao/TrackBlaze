export interface ISchool {
  id: number;
  name?: string | null;
}

export type NewSchool = Omit<ISchool, 'id'> & { id: null };

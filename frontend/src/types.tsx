export type user = {
  username: string;
  email: string;
  role: string;
  createdAt: string;
};

export interface tag {
  id: number;
  name: string;
}

export type problem = {
  id: number;
  title: string;
  body: string;
};
